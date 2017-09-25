package com.ruijc.sevenzip;

import com.ruijc.common.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: sokolov_a
 * Date: 15.02.2011
 * Time: 16:25:12
 * To change this template use File | Settings | File Templates.
 */
public class CoderInfo {
    private long methodID;
    private ByteBuffer props;
    private int numInStreams;
    private int numOutStreams;


    public ByteBuffer getProps() {
        return props;
    }

    public void setProps(ByteBuffer props) {
        this.props = props;
    }

    public boolean isSimpleCoder() {
        return (numInStreams == 1) && (numOutStreams == 1);
    }

    public long getMethodID() {
        return methodID;
    }

    public void setMethodID(long methodID) {
        this.methodID = methodID;
    }

    public int getNumInStreams() {
        return numInStreams;
    }

    public void setNumInStreams(int numInStreams) {
        this.numInStreams = numInStreams;
    }

    public int getNumOutStreams() {
        return numOutStreams;
    }

    public void setNumOutStreams(int numOutStreams) {
        this.numOutStreams = numOutStreams;
    }
}
