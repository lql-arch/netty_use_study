package ChatClient.message;



public class RpcResponseMessage extends Message {
    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }


    public void setExceptionValue(Exception e) {
        this.exceptionValue = e;
    }

    public void setReturnValue(Object invoke) {
        this.returnValue = invoke;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public Exception getExceptionValue() {
        return exceptionValue;
    }
}
