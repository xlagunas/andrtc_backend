package cat.xlagunas.andrtc.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PushMessage {

    @JsonProperty("registration_ids")
    public final List<String> ids;

    @JsonProperty("data")
    public final PushMessageData data;

    private PushMessage() {
        throw new UnsupportedOperationException();
    }

    private PushMessage(Builder builder) {
        this.ids = builder.ids;
        this.data = builder.data;
    }


    public static class Builder {
        private List<String> ids;
        private PushMessageData data;

        public Builder tokenList(List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder content(PushMessageData data) {
            this.data = data;
            return this;
        }

        public PushMessage build() {
            return new PushMessage(this);
        }
    }
}
