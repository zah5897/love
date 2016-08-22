package com.easemob.server.example.comm.body;

import com.easemob.server.example.comm.constant.MsgType;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class CmdMessageBody extends MessageBody {

	public CmdMessageBody(String targetType, String[] targets, String from, Map<String, String> ext) {
		super(targetType, targets, from, ext);
	}

    public ContainerNode<?> getBody() {
        if(!isInit()){
        	ObjectNode msgNode=JsonNodeFactory.instance.objectNode();
        	msgNode.put("type", MsgType.CMD);
        	msgNode.put("action","");
            this.getMsgBody().put("msg", msgNode);
            this.setInit(true);
        }
        return this.getMsgBody();
    }

    public Boolean validate() {
		return super.validate() && StringUtils.isNotBlank(getFrom());
	}
}
