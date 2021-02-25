package id.co.bfi.dmsuploadscheduler.service.share_folder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.bfi.dmsuploadscheduler.config.yaml.JasyptConfig;
import id.co.bfi.dmsuploadscheduler.config.yaml.ShareFolderConfig;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

@Service
public class ShareFolderService {
	
	@Autowired
	private ShareFolderConfig shareFolderConfig;
	
	@Autowired
	private JasyptConfig jasyptConfig;
	
	// test
	public ByteArrayOutputStream getFileOverSharedFolder(String filePath) throws IOException {
		
		final String userName = shareFolderConfig.getUser();
		final String password = jasyptConfig.decryptPassword(shareFolderConfig.getPass());
		final String domain = shareFolderConfig.getDomain();
		final String shareFolderName = shareFolderConfig.getName();
		
		String pathFileName = "\\\\"+domain+"\\"+shareFolderName+"\\"+filePath;
		if (pathFileName.contains("\\\\"))
			pathFileName = pathFileName.replace("\\\\", "//");
		if (pathFileName.contains("\\"))
			pathFileName = pathFileName.replace("\\", "/");
		if (!pathFileName.startsWith("//"))
			pathFileName = "//"+pathFileName;
		if (!pathFileName.startsWith("smb:"))
			pathFileName = "smb:"+pathFileName;
		
		final NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, userName, password);
	    final SmbFile sFile = new SmbFile(pathFileName, auth);
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    final SmbFileInputStream smbFileInputStream = new SmbFileInputStream(sFile);

	    final byte[] buf = new byte[16 * 1024 * 1024];
	    int len;
	    while ((len = smbFileInputStream.read(buf)) > 0) {
	    	baos.write(buf, 0, len);
	    }
	    smbFileInputStream.close();
	    baos.close();
	    return baos;
	}
}
