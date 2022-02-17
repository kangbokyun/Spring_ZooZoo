function likeClick(bno, cano, mno){
    alert("bno : "+bno+"cano : "+cano+ "mno : "+mno);
    $.ajax({
        url: "/Board/Free/ClickFreeBoardLike",
        data:{"bno":bno,"cano":cano,"mno":mno},
        success: function(result) {
            if(result == 1){
                alert("좋아요 등록");
            }
            else if(result == 2){
                alert("좋아요 삭제");
            }
            else {alert("버그 발생!!");}
        }
    });
}