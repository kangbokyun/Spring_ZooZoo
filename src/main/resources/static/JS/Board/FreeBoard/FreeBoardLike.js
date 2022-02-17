function likeClick(bno, cano, mno){
    console.log("bno : "+bno+"cano : "+cano+ "mno : "+mno);
    $.ajax({
        url: "/Board/Free/ClickFreeBoardLike",
        data:{"bno":bno,"cano":cano,"mno":mno},
        success: function(result) {
            alert("success : "+result);
            if(result == 1){
                alert("좋아요 완료");
            }
            else if(result == 2){
                alert("좋아요 삭제");
            }
            else {alert("버그 발생!!")}
        }
    });
}