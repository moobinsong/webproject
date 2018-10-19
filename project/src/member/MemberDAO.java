package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class MemberDAO {
	private Connection con=null;
	private PreparedStatement pstmt=null;
	private ResultSet rs=null;
	private DataSource ds=null;
	
	private Connection getConnection() {
		try {
			Context ctx=new InitialContext();
			ds=(DataSource)ctx.lookup("java:comp/env/jdbc/MySQL");
			con=ds.getConnection();
		} catch (NamingException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return con;
	}
	private void close(Connection con,PreparedStatement pstmt,ResultSet rs) {
		try {
				if(rs!=null)
					rs.close();				
				if(pstmt!=null)
					pstmt.close();
				if(con!=null)
					con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void close(Connection con,PreparedStatement pstmt) {
		try {
				if(con!=null)
					con.close();
				if(pstmt!=null)
					pstmt.close();				
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public int member_insert(MemberVO vo) {
		int result=0;
				
		try {
			con=getConnection();
			//트랜잭션
			con.setAutoCommit(false);
			String sql="insert into member values(?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, vo.getUserid());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getGender());
			pstmt.setString(5, vo.getEmail());
			result=pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {			
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			close(con,pstmt);
		}
		
		return result;
	}
	
	public MemberVO isLogin(String id, String password) {
		//id,password 정보에 해당하는 사용자 확인
		MemberVO vo=null;
		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="select * from member where userid=? and password=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				vo=new MemberVO();
				vo.setUserid(rs.getString("userid"));
				vo.setName(rs.getString("name"));
			}
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			close(con,pstmt,rs);
		}
		
		
		return vo;
	}
	
	public boolean checkId(String userid) {
		boolean flag=false;
		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="select * from member where userid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				flag=true;
			}
			con.commit();
		}catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			close(con,pstmt,rs);
		}
		return flag;
	}
	
}