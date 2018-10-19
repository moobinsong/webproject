package model;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDAO;
import vo.BoardVO;

public class BoardInsertAction implements Action {
	private String path;
	
	
	public BoardInsertAction(String path) {
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
		vo.setName(dataMap.get("name")); 
		vo.setTitle(dataMap.get("title")); 
		vo.setContent(dataMap.get("content")); 
		vo.setPassword(dataMap.get("password")); 
		if(dataMap.get("file")!=null)
			vo.setFile(dataMap.get("file"));
		 
		//db삽입
		BoardDAO dao=new BoardDAO();
		int result=dao.insertArticle(vo);
		
		if(result<0)
			path="view/qna_board_write.jsp";
		
		return new ActionForward(path, true);
	}

}







