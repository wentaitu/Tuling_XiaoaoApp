package moe.haruue.tuling_xiaoaoapp;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage extends DataSupport {
	private boolean type;
	private String msg; //消息内容
	private Date date; //日期
	private String dateStr; //日期的字符串格式

	public ChatMessage() {
	}

	public ChatMessage(boolean type, String msg) {
		super();
		this.type = type;
		this.msg = msg;
		setDate(new Date());
	}

	public String getDateStr() {
		return dateStr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.dateStr = df.format(date);
	}

	public boolean getType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
