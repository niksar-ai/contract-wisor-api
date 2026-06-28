package ai.niksar.contract_wisor_api.model;

import com.jcraft.jsch.SftpATTRS;

public class DocumentSftp {

    private String filename;
    private String longname;
    private SftpATTRS attrs;


    public String getFilename() { return filename; }

    public String getLongname() { return longname; }

    public SftpATTRS getAttrs() { return attrs; }

    public void setFileName(String value) { this.filename = value; }
    public void setLongname(String value) { this.longname = value; }
    public void setAttrs(SftpATTRS value) { this.attrs = value; }

}