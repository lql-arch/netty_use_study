package ChatClient.message;


public class GroupChatRequestMessage extends Message {
    private String content;
    private String groupName;
    private String from;

    public GroupChatRequestMessage(String from, String groupName, String content) {
        this.content = content;
        this.groupName = groupName;
        this.from = from;
    }

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getFrom() {
        return from;
    }

    public String getContent() {
        return content;
    }
}
