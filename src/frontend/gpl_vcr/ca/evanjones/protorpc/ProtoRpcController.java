package ca.evanjones.protorpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

public class ProtoRpcController implements RpcController {
    // Call reset() to initialize everything
    public ProtoRpcController() { reset(); }

    // This is effectively the "constructor" for this class.
    @Override
    public void reset() {
        eventLoop = null;
        builder = null;
        callback = null;
        status = Protocol.Status.INVALID;
    }

    @Override
    public String errorText() {
        throw new UnsupportedOperationException("unimplemented");
    }

    @Override
    public boolean failed() {
        if (status == Protocol.Status.INVALID) {
            String message = "No RPC active.";
            if (callback != null) {
                message = "RPC has not completed.";
            }
            throw new IllegalStateException(message);
        }

        return status != Protocol.Status.OK;
    }

    @Override
    public boolean isCanceled() {
        throw new UnsupportedOperationException("unimplemented");
    }

    @Override
    public void notifyOnCancel(RpcCallback<Object> callback) {
        throw new UnsupportedOperationException("unimplemented");
    }

    @Override
    public void setFailed(String reason) {
        throw new UnsupportedOperationException("unimplemented");
    }

    @Override
    public void startCancel() {
        throw new UnsupportedOperationException("unimplemented");
    }

    /** Wait for the current RPC to complete. */
    public void block() {
        if (status == Protocol.Status.OK) {
            // Assume mockFinishRpcForTest was called.
            return;
        }

        assert eventLoop != null;
        assert callback != null;

        // TODO: If we have a threaded implementation, this will need to be more complicated.
        while (callback != null) {
            eventLoop.runOnce();
        }
    }

    public void startRpc(EventLoop eventLoop, Message.Builder builder, RpcCallback<Message> callback) {
        if (this.callback != null) {
            throw new IllegalStateException(
                    "ProtoRpcController already in use by another RPC call; " +
                    "wait for callback before reusing.");
        }
        if (callback == null) {
            throw new NullPointerException("callback cannot be null");
        }
        assert this.eventLoop == null;
        assert eventLoop != null;
        assert this.builder == null;
        assert builder != null;

        this.eventLoop = eventLoop;
        this.builder = builder;
        this.callback = callback;
        status = Protocol.Status.INVALID;
    }

    /** Finish an RPC for unit tests. */
    // TODO: Create a factory to avoid needing this?
    public void mockFinishRpcForTest() {
        assert status == Protocol.Status.INVALID;
        assert callback == null;
        status = Protocol.Status.OK; 
    }

    public void finishRpc(ByteString response) {
        // Set the status and reset state before we invoke the callback
        status = Protocol.Status.OK;
        eventLoop = null;
        Message.Builder tempBuilder = builder;        
        builder = null;
        RpcCallback<Message> tempCallback = callback;
        callback = null;

        try {
            tempBuilder.mergeFrom(response);
            tempCallback.run(tempBuilder.build());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private EventLoop eventLoop;
    private Message.Builder builder;
    private RpcCallback<Message> callback;
    private Protocol.Status status;
}
