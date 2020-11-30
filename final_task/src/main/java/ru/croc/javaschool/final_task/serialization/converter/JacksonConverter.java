package ru.croc.javaschool.final_task.serialization.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;

/**
 * Конвертер для преобразования данных из/в XML.
 */
public class JacksonConverter {

    /**
     * Получение Java-объекта из XML.
     * @param xml - XML в виде строки.
     * @param type - класс, объект(ы) которого необходимо получить (Class.class).
     * @param <T> - тип объекта(ов), который(е) необходимо получить.
     * @return
     * @throws IOException
     */
    public <T> T fromXml(String xml, Class<T> type) throws IOException {
        XmlMapper mapper = createXmlMapper();
        return mapper.readValue(xml, type);
    }

    /**
     * Преобразование Java-объекта в XML.
     * @param obj - сериализуемый объект.
     * @return - XML в виде строки.
     * @throws JsonProcessingException - ошибка получения XML.
     */
    public String toXml(Object obj) throws JsonProcessingException {
        XmlMapper mapper = createXmlMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * Создание объекта типа xmlMapper, отвечающего за сериализацию/десериализацию.
     * @return - объект типа xmlMapper.
     */
    private XmlMapper createXmlMapper() {
        final XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }
}