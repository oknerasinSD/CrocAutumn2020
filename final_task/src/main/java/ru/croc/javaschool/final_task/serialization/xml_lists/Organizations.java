package ru.croc.javaschool.final_task.serialization.xml_lists;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ru.croc.javaschool.final_task.database.model.Organization;

import java.util.ArrayList;
import java.util.List;

/**
 * Список организация для сериализации.
 */
@JacksonXmlRootElement(localName = "organizations")
public class Organizations {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "organization")
    private List<Organization> organizations = new ArrayList<>();

    public Organizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public Organizations() {
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }
}
