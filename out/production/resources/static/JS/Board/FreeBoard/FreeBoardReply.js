//댓글 쓰기
function freeReplyWrite(bno){
    var frcontents = $("#frcontents").val();
    $.ajax({
        url:"/Board/Free/FreeBoardReplyWrite",
        data:{"bno":bno, "frcontents":frcontents},
        success: function(result) {
           if(result ==1){
               alert("로그인이 필요합니다.")
               location.href = "/Member/Login";
           }else if(result == 2){
               alert("내용을 입력해주세요.");
           }else if(result == 3){
               alert("댓글 등록 완료");
               document.getElementById("frcontents").value="";
               $("#freeReplytable").load(location.href + ' #freeReplytable');
           }else {
               alert("버그 발생!!");
           }
        }
    });
}

//자유게시판 댓글 삭제
function freeReplyDelete(rno){
    alert(rno);
    $.ajax({
        url:"/Board/Free/FreeBoardReplyDelete",
        data:{"rno":rno},
        success: function(result) {
            if(result == 1){
                alert("댓글 삭제 완료");
                 $("#freeReplytable").load(location.href + ' #freeReplytable');
            }else{
                alert("댓글 삭제 실패");
            }
        }
    });
}

//자유게시판 댓글 수정
function rupdate(bno){
    alert(bno);
    document.getElementById("tdbcontents"+bno).innerHTML = "<input class='form-control' type='text' id='newcontents' name='newcontents' style='white-space: nowrap;'>";
    document.getElementById("btnrupdate"+bno).style = "display:none"; // 수정버튼 감추기
    document.getElementById("btnrchange"+bno).style = "display:block"; // 확인버튼 보이기

    $("#btnrchange"+bno).click(function(){ // 확인 버튼 클릭
        $.ajax({
            url: "/Board/Free/FreeBoardReplyUpdate",
            data: {"bno" : bno, "newcontents" : document.getElementById("newcontents").value},
            success : function(result){
                if(result == 1){
                    $("#freeReplytable").load(location.href + ' #freeReplytable');
                    document.getElementById("btnrchange"+bno).style = "display:none"; // 확인버튼 감추기
                    document.getElementById("btnrupdate"+bno).style = "display:block"; // 수정버튼 보이기
                } else {
                    alert("오류발생");
                }
            }
        });
    });
}


/*//대댓글 쓰기
$("#RReplySpread"+rno).click(function(){
 alert("bno : "+ bno + " mno : " + mno + " cano : " + cano +" rno : " + rno);
    //댓글 버튼 누르면 댓글 버튼과 스위치 값 바꾸기

        document.getElementById("reReplyInputBox"+rno).style = "display:block";
        document.getElementById("ii"+rno).value = "1";
    }
    //이미 한번 댓글 버튼 누른 상태에서 한번 더 누르면 댓글 버튼 원래대로 바꾸고, 스위치 값도 원래대로

        document.getElementById("reReplyInputBox"+rno).style = "display:none";
        document.getElementById("ii"+rno).value = "0";
    }
 });*/

//자유게시판 대댓글 작성하기
function reReplyWrite(rno, bno, mno, cano){
    //ii = 0일때, 대댓글 쓰는 div 보이기
    const element = document.getElementById("reReplyInputBox"+rno);
    if(document.getElementById("ii"+rno).value == "0"){
         element.style.cssText = "display:block; border-top:1px solid #cccccc";
         document.getElementById("ii"+rno).value = "1";
         //alert("누르고 나서 ii : "+document.getElementById("ii"+rno).value);
         $("#reReplyWbtn"+rno).click(function(){
             var reReplyContents = document.getElementById("reReplyContents"+rno).value;
             alert(rno+"번째 댓글의 대댓글 내용 : " +reReplyContents);
             $.ajax({
                 url:"/ReReply/ReReplyWrite",
                 data:{"rno":rno, "bno":bno, "mno":mno, "cano":cano, "reReplyContents":reReplyContents},
                 success: function(result) {
                    if(result == 0){
                        alert("이미 없는 게시물입니다.");
                        location.href = "/freeboard";
                    }else if(result == 1){
                        alert("내용을 입력해주시기 바랍니다.");
                    }else if(result == 2){
                        alert("등록완료");
                        document.getElementById("reReplyContents"+rno).value = "";
                        element.style = "display:none";
                        document.getElementById("ii"+rno).value = "0";
                        $("#freeReplytable").load(location.href + ' #freeReplytable');
                    }else{
                        alert("버그 발생");
                    }
                 }
             });
         });

     //답글 버튼 한번 더 누르면 대댓글 쓰는 div 숨기기
    }else if(document.getElementById("ii"+rno).value == "1"){
        element.style = "display:none";
        document.getElementById("ii"+rno).value = "0";
        alert("한번 더 누르고 나서 ii : "+document.getElementById("ii"+rno).value);
    }else{
        alert("몰루");
    }
}
