import { React, useState } from "react";
import axios from "axios";

const FeedType = [
  { id: null, value: "카드 + 번들" },
  { id: "CARD", value: "카드" },
  { id: "BUNDLE", value: "번들" },
];

const CardType = [
  { id: null, value: "모두검색" },
  { id: "CARD_PROBLEM", value: "문제" },
  { id: "CARD_GENERAL", value: "일반" },
  { id: "CARD_LINK", value: "링크" },
];

const CategoryIdBig = [
  { id: null, value: "모두검색" },
  { id: "1", value: "알고리즘" },
  { id: "2", value: "CS" },
  { id: "3", value: "직무" },
  { id: "4", value: "언어" },
  { id: "5", value: "기타" },
];

const CategoryIdSmall1 = [
  { id: "1", value: "대분류 모두검색" },
  { id: "6", value: "그래프탐색 / 트리 / 힙" },
  { id: "7", value: "스택 / 큐 / 정렬" },
  { id: "8", value: "완전탐색 / 이분탐색" },
  { id: "9", value: "탐욕 / 동적계획법 / 해시" },
  { id: "10", value: "기타 (해시 .. 등)" },
];
const CategoryIdSmall2 = [
  { id: "2", value: "대분류 모두검색" },
  { id: "11", value: "수학" },
  { id: "12", value: "컴퓨터구조" },
  { id: "13", value: "운영체제" },
  { id: "14", value: "자료구조" },
  { id: "15", value: "네트워크" },
  { id: "16", value: "데이터베이스" },
  { id: "17", value: "기타" },
];
const CategoryIdSmall3 = [
  { id: "3", value: "대분류 모두검색" },
  { id: "18", value: "면접" },
  { id: "19", value: "백엔드" },
  { id: "20", value: "프론트엔드" },
  { id: "21", value: "인프라" },
  { id: "22", value: "데이터베이스 엔지니어" },
  { id: "23", value: "기타" },
];
const CategoryIdSmall4 = [
  { id: "4", value: "대분류 모두검색" },
  { id: "24", value: "C / C++" },
  { id: "25", value: "JAVA" },
  { id: "26", value: "Python" },
  { id: "27", value: "Kotlin" },
  { id: "28", value: "JS / HTML / CSS" },
  { id: "29", value: "SQL" },
  { id: "30", value: "기타" },
];
const CategoryIdSmall5 = [
  { id: "5", value: "대분류 모두검색" },
  { id: "31", value: "기업분석" },
  { id: "32", value: "IT기사 / 트렌드" },
  { id: "33", value: "자격증" },
  { id: "34", value: "기타" },
];

function CardSearch() {
  // input에 검색어를 입력하면 setSearch 사용하여 search 변경
  const [search, setSearch] = useState("");
  // 피드타입 선택
  const [feedType, setFeedType] = useState("");
  const feedTypeDropbox = (e) => {
    const { value } = e.target;
    setFeedType(FeedType.filter((el) => el.value === value)[0].id);
  };

  // 카드타입 선택
  const [cardType, setCardType] = useState("");
  const cardTypeDropbox = (e) => {
    const { value } = e.target;
    setCardType(CardType.filter((el) => el.value === value)[0].id);
  };

  // 카테고리 선택
  const [categoryId, setCategoryId] = useState("");
  const [categoryId2, setcategoryId2] = useState("");

  // 대분류
  const categoryIdBigDropbox = (e) => {
    const { value } = e.target;
    setCategoryId(CategoryIdBig.filter((el) => el.value === value)[0].id);
    setcategoryId2(CategoryIdBig.filter((el) => el.value === value)[0].id);
  };

  // 중분류 1
  const categoryIdSmall1Dropbox = (e) => {
    const { value } = e.target;
    setCategoryId(CategoryIdSmall1.filter((el) => el.value === value)[0].id);
  };
  // 중분류 2
  const categoryIdSmall2Dropbox = (e) => {
    const { value } = e.target;
    setCategoryId(CategoryIdSmall2.filter((el) => el.value === value)[0].id);
  };
  // 중분류 3
  const categoryIdSmall3Dropbox = (e) => {
    const { value } = e.target;
    setCategoryId(CategoryIdSmall3.filter((el) => el.value === value)[0].id);
  };
  // 중분류 4
  const categoryIdSmall4Dropbox = (e) => {
    const { value } = e.target;
    setCategoryId(CategoryIdSmall4.filter((el) => el.value === value)[0].id);
  };
  // 중분류 5
  const categoryIdSmall5Dropbox = (e) => {
    const { value } = e.target;
    setCategoryId(CategoryIdSmall5.filter((el) => el.value === value)[0].id);
  };

  // clickSearchBtn 실행시켜 회원검색 진행
  const clickSearchBtn = () => {
    axios({
      url: "http://localhost:8080/api/v4/feeds/",
      method: "get",
      params: { feedType, cardType, categoryId, search },
      withCredentials: true,
    }).then((result) => {
      if (result.data) {
        console.log(result.data);
        // const resultId = result.data.userInfo;
        // setResultId(result.data.userInfo);
      }
    });
  };

  return (
    <div>
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        {/* 검색어 입력 */}
        <input
          placeholder="search"
          style={{
            backgroundColor: "rgba(0, 0, 0, 0.2)",
            padding: "16px",
            paddingLeft: "30px",
            borderRadius: "15px",
            border: "0px",
            display: "block",
            margin: "15px",
            width: "70%",
            color: "white",
            fontSize: "18px",
          }}
          onChange={(e) => setSearch(e.target.value)}
        />
        <button
          type="button"
          onClick={clickSearchBtn}
          sx={{
            bgcolor: "#81D8CF",
            color: "#000000",
            fontSize: "midium",
            fontWeight: "bold",
          }}
          variant="contained"
          fullWidth
        >
          검색하기
        </button>
      </div>
      <div>
        {/* 피드타입 */}
        <select onChange={feedTypeDropbox}>
          {FeedType.map((el) => (
            <option key={el.id}>{el.value}</option>
          ))}
        </select>
        {/* 피드타입별 저장값 */}
        <div>
          <p>{feedType}</p>
        </div>

        {feedType === "CARD" ? (
          <>
            {/* 카드타입 */}
            <select onChange={cardTypeDropbox}>
              {CardType.map((el) => (
                <option key={el.id}>{el.value}</option>
              ))}
            </select>
            {/* 카드타입별 저장값 */}
            <div>
              <p>{cardType}</p>
            </div>

            {/* 카테고리 대 */}
            <select onChange={categoryIdBigDropbox}>
              {CategoryIdBig.map((el) => (
                <option key={el.id}>{el.value}</option>
              ))}
            </select>
            {/* 카테고리별 저장값 */}
            <div>
              <p>{categoryId2}</p>
            </div>
            {/*  eslint-disable-next-line */}
            {categoryId2 === "1" ? (
              <>
                {/* 카테고리 중1 */}
                <select onChange={categoryIdSmall1Dropbox}>
                  {CategoryIdSmall1.map((el) => (
                    <option key={el.id}>{el.value}</option>
                  ))}
                </select>
                {/* 카테고리별 저장값1 */}
                <div>
                  <p>{categoryId}</p>
                </div>
              </>
            ) : // eslint-disable-next-line
            categoryId2 === "2" ? (
              <>
                {/* 카테고리 중2 */}
                <select onChange={categoryIdSmall2Dropbox}>
                  {CategoryIdSmall2.map((el) => (
                    <option key={el.id}>{el.value}</option>
                  ))}
                </select>
                {/* 카테고리별 저장값2 */}
                <div>
                  <p>{categoryId}</p>
                </div>
              </>
            ) : // eslint-disable-next-line
            categoryId2 === "3" ? (
              <>
                {/* 카테고리 중3 */}
                <select onChange={categoryIdSmall3Dropbox}>
                  {CategoryIdSmall3.map((el) => (
                    <option key={el.id}>{el.value}</option>
                  ))}
                </select>
                {/* 카테고리별 저장값3 */}
                <div>
                  <p>{categoryId}</p>
                </div>
              </>
            ) : // eslint-disable-next-line
            categoryId2 === "4" ? (
              <>
                {/* 카테고리 중4 */}
                <select onChange={categoryIdSmall4Dropbox}>
                  {CategoryIdSmall4.map((el) => (
                    <option key={el.id}>{el.value}</option>
                  ))}
                </select>
                {/* 카테고리별 저장값4 */}
                <div>
                  <p>{categoryId}</p>
                </div>
              </>
            ) : categoryId2 === "5" ? (
              <>
                {/* 카테고리 중5 */}
                <select onChange={categoryIdSmall5Dropbox}>
                  {CategoryIdSmall5.map((el) => (
                    <option key={el.id}>{el.value}</option>
                  ))}
                </select>
                {/* 카테고리별 저장값5 */}
                <div>
                  <p>{categoryId}</p>
                </div>
              </>
            ) : null}
          </>
        ) : null}
      </div>
    </div>
  );
}

export default CardSearch;
