// Import - react
import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { useSelector } from "react-redux";

// Import - design
import MDTypography from "components/MDTypography";
import { Box, Button, TextField } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import CloseIcon from "@mui/icons-material/Close";
import CheckIcon from "@mui/icons-material/Check";

function HomeCommentList({ handleCommetList, commentList }) {
  // =============================== Data ===============================
  const { loginInfo } = useSelector((state) => state.homeReducer);

  // 댓글 수정 boolean 값
  const [edit, setEdit] = useState({
    id: -1,
    comment: "",
  });

  // ============================== Function =============================
  // function in component
  // (1) 글 수정 버튼
  const editButton = (reply, commentId) => {
    console.log("글 수정 시작");
    setEdit({
      id: commentId,
      comment: reply,
    });
    handleCommetList(); // 목록 다시 불러오기
  };

  // (1-2) 글 수정 정보 local data에 저장
  const handleEdit = (event) => {
    event.preventDefault();
    console.log(event.target.value);

    setEdit({ ...edit, comment: event.target.value });
  };

  // (2) 글 수정 취소 버튼
  const editCloseButton = () => {
    console.log("글 수정 취소");

    setEdit({
      id: -1,
      comment: "",
    });
  };

  // (3) 글 수정 완료 버튼 -> axios로 db에 update 요청
  const editConfirmButton = () => {
    console.log("글 수정 완료");

    // axios로 수정 msg 보내기

    setEdit({
      id: -1,
      comment: "",
    });
  };

  // (4) 글 삭제 버튼 -> axios로 db에 delete 요청
  const deleteButton = () => {
    console.log("글 삭제");
  };

  // useEffect
  useEffect(() => {}, [edit]);

  // ================================ Return ===============================
  return (
    <ul
      style={{
        maxHeight: "200px",
        overflow: "auto",
        overflowX: "hidden",
      }}
    >
      {commentList !== null &&
        commentList.map((comment) => (
          <li key={comment.commentId}>
            <Box sx={{ display: "flex", justifyContent: "space-between" }}>
              <Box>
                {edit.id === comment.commentId ? (
                  <Box sx={{ display: "flex" }}>
                    <MDTypography variant="body2" fontWeight="light">
                      {comment.name}&nbsp;:
                    </MDTypography>
                    <TextField
                      sx={{ ml: 1 }}
                      type="text"
                      size="small"
                      id={`edit-comment-${comment.commentId}`}
                      onChange={handleEdit}
                      defaultValue={comment.commentContent}
                    />
                  </Box>
                ) : (
                  <MDTypography variant="body2" fontWeight="light">
                    {comment.commentWriterNickname} : {comment.commentContent}
                  </MDTypography>
                )}
              </Box>
              {loginInfo.userId === comment.commentWriterId && (
                <Box>
                  {edit.id === comment.commentId ? (
                    <Box>
                      <Button type="button" onClick={editCloseButton} sx={{ p: "0" }}>
                        <CloseIcon />
                      </Button>
                      <Button type="button" onClick={editConfirmButton} sx={{ p: "0" }}>
                        <CheckIcon />
                      </Button>
                    </Box>
                  ) : (
                    <Box>
                      <Button
                        type="button"
                        onClick={() => editButton(comment.reply, comment.id)}
                        sx={{ p: "0" }}
                      >
                        <EditIcon />
                      </Button>
                      <Button type="button" onClick={deleteButton} sx={{ p: "0" }}>
                        <DeleteIcon />
                      </Button>
                    </Box>
                  )}
                </Box>
              )}
            </Box>
          </li>
        ))}
    </ul>
  );
}

HomeCommentList.defaultProps = {
  commentList: [],
};

HomeCommentList.propTypes = {
  handleCommetList: PropTypes.func.isRequired,
  commentList: PropTypes.arrayOf(PropTypes.object),
};

export default HomeCommentList;
