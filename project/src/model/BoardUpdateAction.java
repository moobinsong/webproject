package model;

import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDAO;
import vo.BoardVO;
import vo.SearchVO;

public class BoardUpdateAction implements Action {
	private String path;	
	
	public BoardUpdateAction(String path) {
		super();
		this.path = path;
	}

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//파일 처리를 위해 UploadFileUtils 호출
		UploadFileUtils utils=new UploadFileUtils();
		HashMap<String, String> dataMap=utils.getItem(req);
		
		//dataMap에 담긴 데이터를 꺼내어 db에 삽입하기
		BoardVO vo=new BoardVO();
		vo.setBno(Integer.parseInt(dataMap.get("bno"))); 
		vo.setTitle(dataMap.get("title")); 
		vo.setContent(dataMap.get("content")); 
		vo.setPassword(dataMap.get("password")); 
		if(dataMap.get("file")!=null)
			vo.setFile(dataMap.get("file"));
		
		//current_page값
		String page=dataMap.get("page");
		
		/********검색어 처리 부분때문에 추가**********/		
		SearchVO search=null;
		String criteria=null,keyword=null;
		if(dataMap.get("criteria")!=null) {
			search=new SearchVO();
			criteria=dataMap.get("criteria");
			search.setCriteria(criteria); 
			keyword=URLEncoder.encode(dataMap.get("keyword"), "utf-8");
			search.setKeyword(keyword);
		}
		/*********************/
				
		//데이터베이스 처리
		//① 비밀번호 확인
		BoardDAO dao=new BoardDAO();
		boolean flag=dao.pwdCheck(vo.getBno(), vo.getPassword());
		
		//② 업데이트 실행
		if(flag) {
			dao.updateRow(vo);
			path+="?bno="+vo.getBno()+"&page="+page
					+"&criteria="+criteria+"&keyword="+keyword;
		}else {
			path="view/pwdCheckError.jsp";//qna_board_modify.jsp로 이동
		}		
		return new ActionForward(path,true);
	}

}









