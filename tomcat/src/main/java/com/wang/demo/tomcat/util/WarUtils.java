package com.wang.demo.tomcat.util;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Created by zhongruo
 * Date: 19/12/27
 * Time: 下午4:14
 * Described:
 */
public class WarUtils {
    /**
     * 解压war文件
     *
     * @param warPath   war文件路径
     * @param unZipPath 解压到的路径
     */
    public static boolean unZipWar(Path warPath, Path unZipPath) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(warPath.toFile()));
             ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(
                     ArchiveStreamFactory.JAR, bis)) {
            if (unZipPath != warPath) {
                if (Files.exists(unZipPath)) {
                    Files.delete(unZipPath);
                }
                Files.createDirectories(unZipPath);
            }
            JarArchiveEntry entry;
            while ((entry = (JarArchiveEntry) ais.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    Path currentPath = Paths.get(unZipPath.toString(), entry.getName());
                    if (!Files.exists(currentPath.getParent())) {
                        Files.createDirectories(currentPath);
                    } else if (!Files.exists(currentPath)) {
                        Files.createDirectory(currentPath);
                    }
                } else {
                    Path currentPath = unZipPath.resolve(entry.getName());
                    if (!Files.exists(currentPath.getParent())) {
                        Files.createDirectories(currentPath.getParent());
                    }
                    try (OutputStream os = Files.newOutputStream(currentPath)) {
                        IOUtils.copy(ais, os);
                    }
                }
            }
        } catch (ArchiveException | IOException e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String localPath=Paths.get(".").toAbsolutePath().toFile().getPath();
        System.out.println(localPath);
    }

}
