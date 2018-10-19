package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import vo.BoardVO;

public class BoardDAO {
	
	Connection con=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	
	public Connection getConnection() {
		Context ctx;
		try {
			ctx = new InitialContext();
			DataSource ds=(DataSource)ctx.lookup("java:comp/env/jdbc/MySQL");
			con=ds.getConnection();
		}catch (Exception e) {			
			System.out.println("DB 커넥션 실패"+e);	
		}	
		return con;
	}
	//close()
	private void close(Connection con,PreparedStatement pstmt,
			ResultSet rs) {
		try {
				if(rs!=null)	rs.close();
				if(pstmt!=null)	pstmt.close();			
				if(con!=null)	con.close();			
				}catch(SQLException e) {
					e.printStackTrace();
				}
		}
	private void close(Connection con,PreparedStatement pstmt) {
		try {				
				if(pstmt!=null)		pstmt.close();			
				if(con!=null)		con.close();			
			}catch(SQLException e) {
				e.printStackTrace();
		}
	}
	
	//게시판 전체 목록 가져오기
	public ArrayList<BoardVO> selectAll(){
		//번호, 제목, 작성자, 날짜, 조회수가 보여질 것임
		ArrayList<BoardVO> list=new ArrayList<>();
		
		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="select bno,title,name,regdate,readcount ";
			//sql+="from board order by bno desc";
			sql+="from board order by re_ref desc, re_seq asc";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				BoardVO vo=new BoardVO();
				vo.setBno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setReadcount(rs.getInt(5));
				list.add(vo);
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
		return list;
	}
	
	//게시글 하나 삽입하기
	public int insertArticle(BoardVO vo) {
		int result=0;
		//bno가 현재 auto_increment 상태가 아니므로
		//현재 들어가 있는 글번호를 먼저 가져온 후 +1 하여
		//bno를 결정한다.
		int bno=0;
		try {
			con=getConnection();
			con.setAutoCommit(false);
			pstmt=con.prepareStatement("select max(bno) from board"); 
			rs=pstmt.executeQuery();
			if(rs.next())
				bno=rs.getInt(1)+1;
			else
				bno=1;
			//vo에 들어있는 데이터를 기준으로 insert 작업 실행
			//insert into board(bno,name,password,title,content,
			//		file,re_ref,re_lev,re_seq)
			String sql="insert into board(bno,name,password,title,content,";
			sql+="file,re_ref,re_lev,re_seq) values(?,?,?,?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, bno);
			pstmt.setString(2, vo.getName());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getTitle());
			pstmt.setString(5, vo.getContent());
			pstmt.setString(6, vo.getFile());
			pstmt.setInt(7, bno);
			pstmt.setInt(8, 0);
			pstmt.setInt(9, 0);
			
			result=pstmt.executeUpdate();
			if(result>0)
				con.commit();			
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
		return result;
	}
	
	//bno에 해당하는 글 하나 가져오기
	public BoardVO getRow(int bno){
		BoardVO vo=null;
		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="select bno,title,name,content,file,re_ref,re_lev,re_seq ";
			sql+="from board where bno=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				vo=new BoardVO();
				vo.setBno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setContent(rs.getString(4));
				vo.setFile(rs.getString(5));
				//reply에서 필요한 정보들
				vo.setRe_ref(rs.getInt(6));
				vo.setRe_lev(rs.getInt(7));
				vo.setRe_seq(rs.getInt(8));
				//===여기까지
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
		
		return vo;
	}
	
	//조회수 올리기
	public int setReadCountUpdate(int bno) {
		int result=0;
		
		try {
				con=getConnection();
				con.setAutoCommit(false);
				String sql="update board set readcount=readcount+1 ";
				sql+="where bno=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, bno);
				result=pstmt.executeUpdate();
				if(result>0)
					con.commit();
		}catch(Exception e) {
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
	
	//비밀번호 확인
	public boolean pwdCheck(int bno,String password) {
		boolean flag=false;
		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="select bno from board where bno=? and password=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, bno);
			pstmt.setString(2, password);
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
	//게시글 삭제
	public int deleteRow(int bno) {
		int result=0;

		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="delete from board where bno=?";			
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, bno);
			result=pstmt.executeUpdate();
			if(result>0)
				con.commit();
		}catch(Exception e) {
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
	
	//게시글 수정
	public int updateRow(BoardVO vo) {
		int result=0;
		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="";
			if(vo.getFile()!=null) {
				sql="update board set content=?, title=?, file=?"
						+ " where bno=?";			
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, vo.getContent());
				pstmt.setString(2, vo.getTitle());
				pstmt.setString(3, vo.getFile());
				pstmt.setInt(4, vo.getBno());			
			}else {			
				sql="update board set content=?, title=? where bno=?";			
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, vo.getContent());
				pstmt.setString(2, vo.getTitle());				
				pstmt.setInt(3, vo.getBno());				
			}
			
			result=pstmt.executeUpdate();
			if(result>0)
				con.commit();
		}catch(Exception e) {
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
	
	//답변글 처리
	public int board_reply(BoardVO vo) {
		int result=0;
		
		//답변글도 새글이기 때문에 bno 구하기
		int bno=0;
		try {
			con=getConnection();
			con.setAutoCommit(false);
			pstmt=con.prepareStatement("select max(bno) from board"); 
			rs=pstmt.executeQuery();
			if(rs.next())
				bno=rs.getInt(1)+1;
			else
				bno=1;
		
		//원본글의 re_ref,re_lev,re_seq가져온 후
		//update 실행하기
		int re_ref=vo.getRe_ref();
		int re_lev=vo.getRe_lev();
		int re_seq=vo.getRe_seq();
		//현재 원본글에 대한 기존 댓글들의 re_seq 값 변화
		String sql="update board set re_seq=re_seq+1 ";
		sql+="where re_ref=? and re_seq>?";
		pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, re_ref);
		pstmt.setInt(2, re_seq);
		pstmt.executeUpdate();
			
		//원본글의 re_seq와 re_lev값에 +1을 한 후
		re_seq=re_seq+1;
		re_lev=re_lev+1;
		//댓글의 insert 작업 시작하기
		sql="insert into board(bno,name,password,title,content,";
		sql+="file,re_ref,re_lev,re_seq) values(?,?,?,?,?,?,?,?,?)";
		pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, bno);
		pstmt.setString(2, vo.getName());
		pstmt.setString(3, vo.getPassword());
		pstmt.setString(4, vo.getTitle());
		pstmt.setString(5, vo.getContent());
		pstmt.setString(6, vo.getFile());
		pstmt.setInt(7, re_ref);
		pstmt.setInt(8, re_lev);
		pstmt.setInt(9, re_seq);
		
		result=pstmt.executeUpdate();
		if(result>0)
			con.commit();			
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
		return result;
	}
	
	//페이징 처리 후 게시물 가져오기
	public ArrayList<BoardVO> getList(int page, int limit){
		//사용자가 선택한 페이지 번호를 가지고 몇번째 레코드부터 가져올 것인지 결정
		int start=(page-1)*10;
		
		
		//번호, 제목, 작성자, 날짜, 조회수가 보여질 것임
		ArrayList<BoardVO> list=new ArrayList<>();

		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="select bno,title,name,regdate,readcount,re_lev ";
			sql+="from board order by re_ref desc, re_seq asc limit ?,?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, limit);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				BoardVO vo=new BoardVO();
				vo.setBno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setReadcount(rs.getInt(5));
				vo.setRe_lev(rs.getInt(6));
				list.add(vo);
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
		return list;
	}
	
	//전체 목록 갯수 구하기
	public int getRows() {
		int total_rows=0;
		
		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="select count(*) from board";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next())
				total_rows=rs.getInt(1);
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
		return total_rows;
	}
	//검색기준과 검색어에 따른 전체 행수
	public int getSearchRows(String criteria, String keyword) {
		int total_rows=0;
		
		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="select count(*) from board where ";
			sql+=criteria+" like ?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, "%"+keyword+"%");
			rs=pstmt.executeQuery();
			if(rs.next())
				total_rows=rs.getInt(1);
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
		return total_rows;
	}
	
	//검색결과리스트
	public ArrayList<BoardVO> getSearchList(int page, int limit
			,String criteria,String keyword){
		//사용자가 선택한 페이지 번호를 가지고 몇번째 레코드부터 가져올 것인지 결정
		int start=(page-1)*10;
		
		
		//번호, 제목, 작성자, 날짜, 조회수가 보여질 것임
		ArrayList<BoardVO> list=new ArrayList<>();

		try {
			con=getConnection();
			con.setAutoCommit(false);
			String sql="select bno,title,name,regdate,readcount,re_lev ";
			sql+="from board where "+criteria+" like ?";
			sql+=" order by re_ref desc, re_seq asc limit ?,?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, "%"+keyword+"%");
			pstmt.setInt(2, start);
			pstmt.setInt(3, limit);
			rs=pstmt.executeQuery();			
			while(rs.next()) {
				BoardVO vo=new BoardVO();
				vo.setBno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setReadcount(rs.getInt(5));
				vo.setRe_lev(rs.getInt(6));
				list.add(vo);
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
		return list;
	}
}













