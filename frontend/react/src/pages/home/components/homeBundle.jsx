// react
import { useState } from "react";

// prop-types is a library for typechecking of props
import PropTypes from "prop-types";

// @mui material components
import { Button, Card } from "@mui/material";
// import Modal from "@mui/material/Modal";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";

// Import custom
import ModalCardList from "pages/home/components/modalCardList";
import LikeButton from "pages/home/buttons/likeButton";
import ScrapButton from "pages/home/buttons/scrapButton";

// Card Image
import CardImg from "assets/images/bundler/bundlerRabbit.png";

function HomeBundle({ bundleInfo }) {
  const [cardListModal, setCardListModal] = useState(false);
  const handleCardListOpen = () => setCardListModal(true);
  const handleCardClose = () => setCardListModal(false);

  return (
    <Card sx={{ ml: 2, mb: 3, maxWidth: 800, minHeight: 200, maxHeight: 400 }}>
      <ModalCardList
        open={cardListModal}
        handleCardClose={handleCardClose}
        cardList={bundleInfo.cardBundleQueryDtoList}
        commentList={bundleInfo.bundleCommentResponseList}
      />
      <MDBox mx={3} sx={{ position: "realative" }}>
        <MDBox display="flex" sx={{ flexWrap: "wrap", justifyContent: "space-between" }}>
          <MDBox display="flex" sx={{ alignItems: "center", width: "80%" }}>
            <MDBox
              component="img"
              src={CardImg}
              alt={CardImg}
              borderRadius="lg"
              shadow="md"
              width="70px"
              height="70px"
              zIndex={1}
            />
            <MDBox mx={2} width="70%">
              <MDTypography variant="h4" textTransform="capitalize" fontWeight="bold">
                [번들]
              </MDTypography>
              <MDTypography variant="overline" mt={1}>
                {bundleInfo.bundleWriterNickname}
              </MDTypography>
            </MDBox>
          </MDBox>
          <MDBox display="flex" m="1" sx={{ alignItems: "center", width: "20%" }}>
            <LikeButton />
            <ScrapButton feedType={bundleInfo.feedType} targetId={bundleInfo.bundleId} />
          </MDBox>
        </MDBox>
        <MDBox mt={2} mb={3}>
          <MDTypography display="inline" variant="h6" textTransform="capitalize" fontWeight="bold">
            {bundleInfo.feedTitle}
          </MDTypography>
        </MDBox>
        <MDBox mt={2} mb={3}>
          <MDTypography variant="body2" component="p" color="text">
            {bundleInfo.feedContent}
          </MDTypography>
        </MDBox>
        {bundleInfo.cardBundleQueryDtoList !== null && (
          <Button onClick={handleCardListOpen}>번들 상세보기</Button>
        )}
      </MDBox>
    </Card>
  );
}

// Default Vlaue
HomeBundle.defaultProps = {
  bundleInfo: {
    createdAt: "",
    bundleThumbnail: "",
    bundleThumbnailText: "",
    bundleWriterProfileImage: "",
    bundleCommentResponseList: [],
    cardBundleQueryDtoList: [],
  },
};

// Typechecking props for the SimpleBlogCard
HomeBundle.propTypes = {
  bundleInfo: PropTypes.shape({
    bundleId: PropTypes.number.isRequired,
    feedType: PropTypes.string.isRequired,
    bundleWriterId: PropTypes.string.isRequired,
    bundleWriterNickname: PropTypes.string.isRequired,
    feedTitle: PropTypes.string.isRequired,
    feedContent: PropTypes.string.isRequired,
    feedLikeCnt: PropTypes.number.isRequired,
    feedCommentCnt: PropTypes.number.isRequired,
    bundlePrivate: PropTypes.bool.isRequired,
    bundleDefault: PropTypes.bool.isRequired,
    createdAt: PropTypes.string,
    bundleThumbnail: PropTypes.string,
    bundleThumbnailText: PropTypes.string,
    bundleWriterProfileImage: PropTypes.string,
    bundleCommentResponseList: PropTypes.arrayOf(PropTypes.object),
    cardBundleQueryDtoList: PropTypes.arrayOf(PropTypes.object),
  }),
};

export default HomeBundle;
