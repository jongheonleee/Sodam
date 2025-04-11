import ProjectsForm from "../../components/v2/ProjectsForm";
import Header from "../../components/Header"
import Footer from "../../components/Footer"
import React from "react";


interface ProjectsProps {
    handleLogout : (e: React.MouseEvent<HTMLAnchorElement | HTMLButtonElement>) => void
}

export default function Projects({
    handleLogout,
}: ProjectsProps) {

    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <ProjectsForm

            />
            <Footer />
        </>
    )
}