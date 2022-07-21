package ChatClient.message;


public class GroupQuitRequestMessage extends Message {
    private String groupName;

    private String username;

    public GroupQuitRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUsername() {
        return username;
    }
}
