package com.imooc.passbook_user.dao;

import com.imooc.passbook_user.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.ws.rs.GET;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * PassTemplate Token Upload
 */
@Slf4j
@Controller
public class TokenUploadController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping("/token")
    public String tokenFileUpload(@RequestParam("merchantsId") String merchantsId,
                                  @RequestParam("passTemplateId") String passTemplateId,
                                  @RequestParam("file")MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        //TODO
        // 判读merchantsId 已经在平台注册且通过审核

        if (null == passTemplateId || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message",
                    "passTempalteId is null or file is empty"
            );
            return "redirect:/uploadStatus";
        }

        try {
            File cur = new File(Constants.TOKEN_DIR + merchantsId);
            if (!cur.exists()) {
                log.info("Create File : {}", cur.mkdir());
            }
            Path path = Paths.get(Constants.TOKEN_DIR, merchantsId, passTemplateId);
            Files.write(path, file.getBytes());

            if (!writeTokenToRedis(path, passTemplateId)) {
                redirectAttributes.addFlashAttribute("message",
                        "write token error"
                );
            } else {
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded" + file.getOriginalFilename()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    /**
     * 将token写入redis
     * @param path {@link Path}
     * @param key redis key
     * @return
     */
    private boolean writeTokenToRedis(Path path, String key) {

        Set<String> tokens;

        try (Stream<String> stringStream = Files.lines(path)){
            tokens = stringStream.collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!CollectionUtils.isEmpty(tokens)) {
            stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String token : tokens) {
                    connection.sAdd(key.getBytes(), token.getBytes());
                }
                return null;
            });

            return true;
        }
        return false;
    }

}
