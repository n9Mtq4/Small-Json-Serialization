package com.n9mtq4.jsonserialzation;

import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;
import lzma.streams.LzmaOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by will on 6/14/16 at 5:27 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public class CompressStream {
	
	private CompressStream() {}
	
	public static LzmaInputStream readLzmaStream(final InputStream receiver, final OutputStream output) throws IOException {
		final LzmaInputStream input = new LzmaInputStream(receiver, new Decoder());
		StreamCopy.copy(input, output);
		return input;
	}
	
	public static LzmaOutputStream writeLzmaStream(final OutputStream receiver, final InputStream input) throws IOException {
		final LzmaOutputStream out = new LzmaOutputStream.Builder(receiver).build();
		StreamCopy.copy(input, out);
		return out;
	}
	
	public static void compressStringToFile(final String str, final File file) throws IOException {
		
		final FileOutputStream fileOutputStream = new FileOutputStream(file);
		final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
		
		final LzmaOutputStream lzmaStream = writeLzmaStream(bufferedOutputStream, byteArrayInputStream);
		
		lzmaStream.close();
		bufferedOutputStream.close();
		byteArrayInputStream.close();
		fileOutputStream.close();
		
	}
	
	public static String readCompressedStringFromFile(final File file) throws IOException {
		
		final FileInputStream fileInputStream = new FileInputStream(file);
		final BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		
		final LzmaInputStream lzmaStream = readLzmaStream(bufferedInputStream, bufferedOutputStream);
		
		bufferedOutputStream.flush();
		final String str = byteArrayOutputStream.toString("UTF-8");
		
		lzmaStream.close();
		fileInputStream.close();
		bufferedInputStream.close();
		byteArrayOutputStream.close();
		bufferedOutputStream.close();
		
		return str;
		
	}
	
}
