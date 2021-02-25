package com.msi.upload.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

@Service
public class ShareFolderService {
	public ByteArrayOutputStream getFileOverSharedFolder(final String domain, final String userName,
	    final String password, final String filePath) throws IOException {
		
		final NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, userName, password);
	    final SmbFile sFile = new SmbFile(filePath, auth);
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
