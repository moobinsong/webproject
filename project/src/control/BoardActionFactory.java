package control;


import model.Action;
import model.BoardDeleteAction;
import model.BoardHitUpdateAction;
import model.BoardInsertAction;
/*import model.BoardDeleteAction;
import model.BoardHitUpdateAction;
import model.BoardInsertAction;
import model.BoardListAction;
import model.BoardModifyAction;
import model.BoardPwdCheckAction;
import model.BoardReplyAction;
import model.BoardReplyViewAction;
import model.BoardSearchAction;
import model.BoardUpdateAction;
import model.BoardViewAction;*/
import model.BoardListAction;
import model.BoardModifyAction;
import model.BoardPwdCheckAction;
import model.BoardReplyAction;
import model.BoardReplyViewAction;
import model.BoardSearchAction;
import model.BoardUpdateAction;
import model.BoardViewAction;

public class BoardActionFactory {
	
	private static BoardActionFactory baf=null;
	
	private BoardActionFactory() {}
	public static BoardActionFactory getInstance() {
		if(baf==null)
			baf=new BoardActionFactory();
		return baf;
	}
	
	Action action=null;
	public Action action(String cmd) {
		
		if(cmd.equals("/qList.do")) {
			action=new BoardListAction("view/qna_board_list.jsp");
		}else if(cmd.equals("/qInsert.do")) {
			action=new BoardInsertAction("qList.do");
		}else if(cmd.equals("/qView.do")) {
			action=new BoardViewAction("view/qna_board_view.jsp");
		}else if(cmd.equals("/qHitUpdate.do")) {
			action=new BoardHitUpdateAction("qView.do");
		}else if(cmd.equals("/qPwdCheck.do")) {
			action=new BoardPwdCheckAction("qDelete.do");
		}else if(cmd.equals("/qDelete.do")) {
			action=new BoardDeleteAction("qList.do");
		}else if(cmd.equals("/qModify.do")){
			action=new BoardModifyAction("view/qna_board_modify.jsp");		
		}else if(cmd.equals("/qUpdate.do")) {
			action=new BoardUpdateAction("qView.do");
		}else if(cmd.equals("/qReplyView.do")) {
			action=new BoardReplyViewAction("view/qna_board_reply.jsp");
		}else if(cmd.equals("/qReply.do")) {
			action=new BoardReplyAction("qList.do");
		}else if(cmd.equals("/qSearch.do")) {
			System.out.println("검색어");
			action=new BoardSearchAction("view/qna_board_list.jsp");
		}
		return action;
	}
}



