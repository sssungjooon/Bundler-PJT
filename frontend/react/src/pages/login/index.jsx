// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";

// react-router-dom components
import { Link } from "react-router-dom";
// @mui material components
import Card from "@mui/material/Card";
import { Button } from "@mui/material/";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import "./index.css";

// Images
import Logo from "assets/images/bundler/loginlogo.png";

// import template
import SignIn from "./signIn";
import SignOut from "./signOut";
// import SignTemplate from "./template/signTemplate";
// import App from "./template/App";

function AuthLogin() {
  return (
    <div className="container">
      <DashboardLayout>
        <DashboardNavbar />
        <Card sx={{ minWidth: 500, maxWidth: 600 }}>
          <div className="container4">
            <Link to="pages\home">
              <img src={Logo} alt="mainlogo" width={250} />
            </Link>
          </div>
          <div className="container5">
            {/* 로그인 버튼 */}
            <MDBox mt={0} mb={1}>
              <SignIn />
            </MDBox>
          </div>
          <div className="container5">
            {/* 회원가입 버튼 */}
            <MDBox mt={0} mb={1} textAlign="center">
              <Link to="/signup">
                <Button
                  sx={{
                    bgcolor: "#FFFFFF",
                    color: "#000000",
                    fontSize: "midium",
                    fontWeight: "bold",
                  }}
                  variant="contained"
                  fullWidth
                >
                  회원가입
                </Button>
              </Link>
            </MDBox>
          </div>
        </Card>

        <button type="button" onClick={SignOut}>
          logout
        </button>
      </DashboardLayout>
    </div>
  );
}

export default AuthLogin;
