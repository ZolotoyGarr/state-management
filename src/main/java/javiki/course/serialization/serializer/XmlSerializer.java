package javiki.course.serialization.serializer;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;


public class XmlSerializer extends AbstractStateSerializer {
    public XmlSerializer() {
        super(".xml", new XmlMapper());
    }
}
