package cat.xlagunas.andrtc.push;

import java.util.List;

class PushMessage {

    public final List<String> registration_ids;

    public final PushMessageData data;

    private PushMessage() {
        throw new UnsupportedOperationException();
    }

    private PushMessage(Builder builder) {
        this.registration_ids = builder.ids;
        this.data = builder.data;
    }

    static class Builder {
        private List<String> ids;
        private PushMessageData data;

        Builder tokenList(List<String> ids) {
            this.ids = ids;
            return this;
        }

        Builder content(PushMessageData data) {
            this.data = data;
            return this;
        }

        PushMessage build() {
            return new PushMessage(this);
        }
    }
}
