package fr.minemobs.manifestapi;

public record Project(int projectID, int fileID, boolean required) {

    public String getProjectID() {
        return String.valueOf(projectID);
    }

    public String getFileID() {
        return String.valueOf(fileID);
    }
}
