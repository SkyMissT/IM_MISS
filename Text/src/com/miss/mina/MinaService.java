 package com.miss.mina;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import com.miss.biz.UserBiz;
import com.miss.biz.impl.UserBizImpl;
import com.miss.entity.Label;
import com.miss.entity.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class MinaService {
	
	static IoAcceptor acceptor;
	
	public MinaService(){
		acceptor = new NioSocketAcceptor();
		initMina();
	}
	
	private void initMina(){
		//添加日志过滤器
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acceptor.setHandler(new MinaServerHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		try {
			acceptor.bind(new InetSocketAddress(9221));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("启动服务");		
	}

}
class MinaServerHandler extends IoHandlerAdapter{
	
	 //---发送
    public static final String TYPE_LOGIN = "2011";
    public static final String TYPE_REGISTER = "2012";
    public static final String TYPE_MESSAGE = "2013";
    public static final String TYPE_RECOMMEND = "2014";
    public static final String TYPE_INFORMATION = "2015";
    public static final String TYPE_UPDATAINFORMATION = "2016";
    // --接收
    public static final String RECIEVE_LOGIN = "1011";
    public static final String RECIEVE_REGISTER = "1012";
    public static final String RECIEVE_MESSAGE = "1013";
    public static final String RECIEVE_RECOMMEND = "1014";
    public static final String RECIEVE_INFORMATION = "1015";
    public static final String RECIEVE_UPDATAINFORMATION = "1016";
    
    //---BIZ
    UserBiz userBiz = new UserBizImpl();
	
	//服务器与客户端创建连接
			@Override
			public void sessionCreated(IoSession session) throws Exception {
				System.out.println("服务器与客户端创建连接...");
				System.out.println("sessionID:" + session.getId());
				super.sessionCreated(session);
			}

			
			@Override
			public void sessionOpened(IoSession session) throws Exception {
				System.out.println("服务器与客户端连接打开...");
				super.sessionOpened(session);
				
			}
			
			//消息的接收处理
			//	TODO : 此处数据处理
			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				super.messageReceived(session, message);
				
				//-------------------正式-----------------------------------------------
				
				JSONObject recieveJson = JSONObject.fromObject(message.toString());
				System.out.println(message.toString());
				String typeRecieve = recieveJson.getString("type");
				String account = recieveJson.getString("account");
				System.out.println("sessionID:" + session.getId() + "    ；发送的消息为：" + message );
				System.out.println("typeRecieve:" + typeRecieve);
				
				//	加入session
				SessionMap.getInstance().addSession(account, session); 
				
				JSONObject replyJson = new JSONObject();
				
				switch(typeRecieve){
//		登陆	
				case RECIEVE_LOGIN:	
					System.out.println("收到"+account + "的消息：" + message);
					replyJson.put("type", TYPE_LOGIN);
					
					String pwd = recieveJson.getString("pwd");
					User userLogin = new User();
					userLogin.setAccount(account);
					userLogin.setPwd(pwd);
					if(userBiz.login(userLogin)){
						//	登陆成功
						replyJson.put("login", "1");
						User u = userBiz.queryUserByAccount(account);
						replyJson.put("account", u.getAccount());
						replyJson.put("nickname", u.getNickname());
						replyJson.put("sex", u.getSex());
						replyJson.put("age", u.getAge());
						replyJson.put("interest", u.getInterest());
						replyJson.put("interestperson", u.getInterestperson());
						replyJson.put("address",u.getAddress() );
						replyJson.put("signature", u.getSignature());
						replyJson.put("icaddress", u.getIcaddress());
						replyJson.put("labels", userBiz.queryLabelByAccount(account));
					}else{
//						登陆失败
						replyJson.put("login", "0");
					}
					session.write(replyJson.toString());
					System.out.println("返回消息" + replyJson.toString());
					break;
//		注册					 
				case RECIEVE_REGISTER:	
					
					replyJson.put("type", TYPE_REGISTER);
					System.out.println("---注册---");
					
					User user = new User();
					user.setAccount(account);
					user.setPwd(recieveJson.getString("pwd"));
					user.setPhonenumb("110");
					user.setEmail("123@qq.com");
					user.setNickname(recieveJson.getString("account"));
					user.setSex("man");
					user.setAge(18);
					user.setInterest("读书");
					user.setInterestperson("上知天文，下知地理，诙谐幽默");
					user.setAddress("china");
					user.setSignature("走自己的路，让别人无路可走");
					user.setIcaddress("ic");
					
					switch (userBiz.createUser(user)) {
					case 0:
						System.out.println("注册成功");
						replyJson.put("issucceed", "0");
						Label label = new Label();
						label.setAccount(account);
						label.setLabel("all");
						userBiz.addLabel(label);
						break;
					case 1:
						System.out.println("注册失败");
						replyJson.put("issucceed", "1");
						break;
					case 2:
						System.out.println("已经存在");
						replyJson.put("issucceed", "2");
						break;
					}
					session.write(replyJson.toString());
					break;
//		聊天信息互发
				case RECIEVE_MESSAGE:
					
					
					String receiver = recieveJson.getString("receiver");
					String content = recieveJson.getString("content");
					
					JSONObject sendJson = new JSONObject();
					sendJson.put("type", TYPE_MESSAGE);
					sendJson.put("account", account);
					sendJson.put("receiver", receiver);
					sendJson.put("content", content);
					System.out.println(account + " 给 "+ receiver+" 发来消息：" + content); 
					if (receiver.length() > 0 ){
						SessionMap.getInstance().sendMessage(receiver, sendJson.toString());
					}
					
					break;
//		推荐
				case RECIEVE_RECOMMEND:
					
					replyJson.put("type", TYPE_RECOMMEND);
					System.out.println("开始推荐");
					
					String labels = recieveJson.getString("label");
					System.out.println("标签"+ labels);
					String address = recieveJson.getString("address");
					int count = recieveJson.getInt("refreshCount");
					String[] listLabel = labels.split(",");
					List<User> list = userBiz.queryUserAccordLabel(listLabel, count);
					JSONArray jsonArray = new JSONArray();
					for(int i=0;i<list.size();i++){
						User currentUser = list.get(i);
						JSONObject json = new JSONObject();
						json.put("account", currentUser.getAccount());
						json.put("nickname", currentUser.getNickname());
						json.put("age", currentUser.getAge());
						json.put("signature", currentUser.getSignature());
						json.put("interest", currentUser.getInterest());
						json.put("interestperson", currentUser.getInterestperson());
						json.put("labels", userBiz.queryLabelByAccount(currentUser.getAccount()));
						json.put("icaddress", currentUser.getIcaddress());
						jsonArray.add(json);
					}
					replyJson.put("verticalData", jsonArray.toString());
					if(count<1){
						JSONArray array = new JSONArray();
						List<User> newUserList = userBiz.queryNewUser();
						for(int i=0;i<newUserList.size();i++){
							User currentUser = newUserList.get(i);
							JSONObject json = new JSONObject();
							json.put("account", currentUser.getAccount());
							json.put("nickname", currentUser.getNickname());
							json.put("age", currentUser.getAge());
							json.put("signature", currentUser.getSignature());
							json.put("interest", currentUser.getInterest());
							json.put("interestperson", currentUser.getInterestperson());
							String label = userBiz.queryLabelByAccount(currentUser.getAccount());
							json.put("labels",label );
							json.put("icaddress", currentUser.getIcaddress());
							array.add(json);
						}
						replyJson.put("horizonData", array.toString());
						System.out.println("推荐结束");
					}
					session.write(replyJson.toString());
					
					break;
//		详细信息
				case RECIEVE_INFORMATION:
					
					System.out.println("详细信息准备····");
					
					replyJson.put("type", TYPE_INFORMATION);
					
					account = recieveJson.getString("account");
					
					User u = userBiz.queryUserByAccount(account);
					replyJson.put("account", u.getAccount());
					replyJson.put("nickname", u.getNickname());
					replyJson.put("sex", u.getSex());
					replyJson.put("age", u.getAge());
					replyJson.put("interest", u.getInterest());
					replyJson.put("interestperson", u.getInterestperson());
					replyJson.put("address",u.getAddress() );
					replyJson.put("signature", u.getSignature());
					replyJson.put("icaddress", u.getIcaddress());
					replyJson.put("labels", userBiz.queryLabelByAccount(account));
					session.write(replyJson.toString());
					System.out.println("account:返回成功····" );
					break;
				case RECIEVE_UPDATAINFORMATION:
					replyJson.put("type", TYPE_UPDATAINFORMATION);
					System.out.println("更改信息准备····");
					
					User updataUser = userBiz.queryUserByAccount(account);
					updataUser.setNickname(recieveJson.getString("nickname"));
					updataUser.setInterest(recieveJson.getString("interest"));
					updataUser.setInterestperson(recieveJson.getString("interestPerson"));
					updataUser.setAddress(recieveJson.getString("address"));
					updataUser.setSex(recieveJson.getString("sex"));
					updataUser.setAge(recieveJson.getInt("age"));
					updataUser.setSignature(recieveJson.getString("signature"));
					
					String label = recieveJson.getString("labels");
					List<Label> listLabel2 = new ArrayList<>();
					String [] ll = label.split(",");
					if(ll.length>0){
						for(int i=0;i<ll.length;i++){
							Label l = new Label();
							l.setAccount(account);
							l.setLabel(ll[i]);
							listLabel2.add(l);
						}
						
					}
					
					if(userBiz.updataUser(updataUser)&&userBiz.initLabel(listLabel2)==0){
						replyJson.put("updata", "ture");
					}else{
						replyJson.put("updata", "false");
					}
					session.write(replyJson.toString());
					break;
				
				}
				
				
			}
			
			@Override
			public void messageSent(IoSession session, Object message)
					throws Exception {
				// TODO Auto-generated method stub
				super.messageSent(session, message);
			}
			
			@Override
			public void sessionClosed(IoSession session) throws Exception {
				long id = session.getId();
				System.out.println("会话关闭--id：" + id);
				super.sessionClosed(session);
			}
	
}
