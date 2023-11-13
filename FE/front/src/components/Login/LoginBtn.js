import React, { Component } from "react";
import SignIn from "./SignIn";
import "../../styles/components/Login/LoginBtn.css";

class LoginBtn extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isModalOpen: false,
    };
  }

  openModal = () => {
    this.setState({ isModalOpen: true });
  };

  closeModal = () => {
    this.setState({ isModalOpen: false });
  };

  render() {
    return (
      <>
        <button className="to_login" onClick={this.openModal}>로그인/회원가입</button>
        <SignIn isOpen={this.state.isModalOpen} close={this.closeModal} />
      </>
    );
  }
}

export default LoginBtn;
