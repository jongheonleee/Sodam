import UserUpdateForm from "../../components/v2/UserUpdateForm";
import Header from "../../components/Header";
import React from "react";
import Footer from "../../components/Footer";

const EditProfile = () => {
    const handleLogout = () => {

    }
    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <UserUpdateForm />
            <Footer/>
        </>
    )
}

export default EditProfile;
