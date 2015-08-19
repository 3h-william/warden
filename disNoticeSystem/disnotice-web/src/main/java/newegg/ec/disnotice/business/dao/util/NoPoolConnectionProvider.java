package newegg.ec.disnotice.business.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Environment;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.classloading.spi.ClassLoadingException;
import org.hibernate.service.jdbc.connections.internal.ConnectionProviderInitiator;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Stoppable;
import org.jboss.logging.Logger;

public class NoPoolConnectionProvider implements ConnectionProvider, Configurable, Stoppable, ServiceRegistryAwareService {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8303828851721899830L;

	private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, NoPoolConnectionProvider.class.getName());

	private String url;
	private Properties connectionProps;
	private Integer isolation;
	private int poolSize;
	private boolean autocommit;

	private final ArrayList<Connection> pool = new ArrayList<Connection>();
	private int checkedOut = 0;

	private boolean stopped;

	private transient ServiceRegistryImplementor serviceRegistry;

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return ConnectionProvider.class.equals(unwrapType) || DriverManagerConnectionProviderImpl.class.isAssignableFrom(unwrapType);
	}

	@Override
	@SuppressWarnings( { "unchecked" })
	public <T> T unwrap(Class<T> unwrapType) {
		if (ConnectionProvider.class.equals(unwrapType) || DriverManagerConnectionProviderImpl.class.isAssignableFrom(unwrapType)) {
			return (T) this;
		} else {
			throw new UnknownUnwrapTypeException(unwrapType);
		}
	}

	public void configure(Map configurationValues) {
		LOG.usingHibernateBuiltInConnectionPool();

		String driverClassName = (String) configurationValues.get(AvailableSettings.DRIVER);
		if (driverClassName == null) {
			LOG.jdbcDriverNotSpecified(AvailableSettings.DRIVER);
		} else if (serviceRegistry != null) {
			try {
				serviceRegistry.getService(ClassLoaderService.class).classForName(driverClassName);
			} catch (ClassLoadingException e) {
				throw new ClassLoadingException("Specified JDBC Driver " + driverClassName + " class not found", e);
			}
		}
		// guard dog, mostly for making test pass
		else {
			try {
				// trying via forName() first to be as close to DriverManager's
				// semantics
				Class.forName(driverClassName);
			} catch (ClassNotFoundException cnfe) {
				try {
					ReflectHelper.classForName(driverClassName);
				} catch (ClassNotFoundException e) {
					throw new HibernateException("Specified JDBC Driver " + driverClassName + " class not found", e);
				}
			}
		}

		poolSize = ConfigurationHelper.getInt(AvailableSettings.POOL_SIZE, configurationValues, 20); // default
																										// pool
																										// size
																										// 20
		LOG.hibernateConnectionPoolSize(poolSize);

		autocommit = ConfigurationHelper.getBoolean(AvailableSettings.AUTOCOMMIT, configurationValues);
		LOG.autoCommitMode(autocommit);

		isolation = ConfigurationHelper.getInteger(AvailableSettings.ISOLATION, configurationValues);
		if (isolation != null)
			LOG.jdbcIsolationLevel(Environment.isolationLevelToString(isolation.intValue()));

		url = (String) configurationValues.get(AvailableSettings.URL);
		if (url == null) {
			String msg = LOG.jdbcUrlNotSpecified(AvailableSettings.URL);
			LOG.error(msg);
			throw new HibernateException(msg);
		}

		connectionProps = ConnectionProviderInitiator.getConnectionProperties(configurationValues);

		LOG.usingDriver(driverClassName, url);
		// if debug level is enabled, then log the password, otherwise mask it
		if (LOG.isDebugEnabled())
			LOG.connectionProperties(connectionProps);
		else
			LOG.connectionProperties(ConfigurationHelper.maskOut(connectionProps, "password"));
	}

	public void stop() {
		LOG.cleaningUpConnectionPool(url);

		for (Connection connection : pool) {
			try {
				connection.close();
			} catch (SQLException sqle) {
				LOG.unableToClosePooledConnection(sqle);
			}
		}
		pool.clear();
		stopped = true;
	}

	public Connection getConnection() throws SQLException {
		LOG.tracev("Total checked-out connections: {0}", checkedOut);

		// essentially, if we have available connections in the pool, use one...
		synchronized (pool) {
			if (!pool.isEmpty()) {
				int last = pool.size() - 1;
				LOG.tracev("Using pooled JDBC connection, pool size: {0}", last);
				Connection pooled = pool.remove(last);
				pooled.close();
				Connection conn = DriverManager.getConnection(url, connectionProps);
//				if (isolation != null) {
//					pooled.setTransactionIsolation(isolation.intValue());
//				}
//				if (pooled.getAutoCommit() != autocommit) {
//					pooled.setAutoCommit(autocommit);
//				}
				checkedOut++;
				return conn;
			}
		}

		// otherwise we open a new connection...

		LOG.debug("Opening new JDBC connection");
		Connection conn = DriverManager.getConnection(url, connectionProps);
		if (isolation != null) {
			conn.setTransactionIsolation(isolation.intValue());
		}
		if (conn.getAutoCommit() != autocommit) {
			conn.setAutoCommit(autocommit);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debugf("Created connection to: %s, Isolation Level: %s", url, conn.getTransactionIsolation());
		}

		checkedOut++;
		return conn;
	}

	public void closeConnection(Connection conn) throws SQLException {
		checkedOut--;

		// add to the pool if the max size is not yet reached.
		synchronized (pool) {
			int currentSize = pool.size();
			if (currentSize < poolSize) {
				LOG.tracev("Returning connection to pool, pool size: {0}", (currentSize + 1));
				conn.close();
				pool.add(conn);
				return;
			}
		}

		LOG.debug("Closing JDBC connection");
		conn.close();
	}

	@Override
	protected void finalize() throws Throwable {
		if (!stopped) {
			stop();
		}
		super.finalize();
	}

	public boolean supportsAggressiveRelease() {
		return false;
	}

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
}