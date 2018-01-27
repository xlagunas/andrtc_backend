package cat.xlagunas.andrtc.repository.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.assertj.core.api.Assertions.assertThat;

public class PushMessageDataTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void whenCreatedThroughBuilder_thenObjectHasRightValues() {
        PushMessageData message = getTestMessage();

        assertThat(message.eventType).isEqualTo(PushMessageData.MessageType.ACCEPT_CALL);
        assertThat(message.params).hasSize(2);
        assertThat(message.params).extracting("firstParam").contains(15).hasSize(1);
        assertThat(message.params).extracting("secondParam").contains("anotherValue").hasSize(1);
    }

    @Test
    public void whenSerialized_thenAllPropertiesFound() throws IOException {
        Writer writer = new StringWriter();
        PushMessageData testMessage = getTestMessage();
        mapper.writeValue(writer, testMessage);

        System.out.println(writer.toString());

        PushMessageData message = mapper.readValue(writer.toString(), PushMessageData.class);
        assertThat(message).isEqualToComparingFieldByField(testMessage);
    }

    private PushMessageData getTestMessage() {
        return new PushMessageData
                .Builder(PushMessageData.MessageType.ACCEPT_CALL)
                .addParams("firstParam", 15)
                .addParams("secondParam", "anotherValue")
                .build();

    }


}