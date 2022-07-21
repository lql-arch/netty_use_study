package ChatClient.message;


public class GroupJoinRequestMessage extends Message {
    private String groupName;

    private String username;

    public GroupJoinRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUsername() {
        return username;
    }
}
