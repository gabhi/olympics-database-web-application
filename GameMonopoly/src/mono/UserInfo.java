package mono;


class UserInfo{
	private String UserName;
	private int UserId;
	
	public UserInfo(){
		UserName = "";
		UserId = -1;
	}
	
	public void SetUserName(String username){
		UserName = username;
	}
	public void SetUserId(int id){
		UserId = id;
	}
	public int GetUserId(){
		return UserId;
	}
	public String GetUserName(){
		return UserName;
	}
}
