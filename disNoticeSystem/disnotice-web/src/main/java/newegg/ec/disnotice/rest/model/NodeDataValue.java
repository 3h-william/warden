package newegg.ec.disnotice.rest.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wz68 on 2015/8/1.
 */
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NodeDataValue {
    private List<String> defaultChooseValueList;
    private String value;
    private String description;

    public NodeDataValue() {
    }

    public NodeDataValue(List<String> defaultChooseValueList, String value, String description) {
        this.defaultChooseValueList = defaultChooseValueList;
        this.value = value;
        this.description = description;
    }

    public List<String> getDefaultChooseValueList() {
        return defaultChooseValueList;
    }

    public void setDefaultChooseValueList(List<String> defaultChooseValueList) {
        this.defaultChooseValueList = defaultChooseValueList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}