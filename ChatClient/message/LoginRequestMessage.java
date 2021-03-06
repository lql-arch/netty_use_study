package ChatClient.message;


public class LoginRequestMessage extends Message {
    private String username;
    private String password;

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
