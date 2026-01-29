package tw.com.tbb.central.tw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.com.tbb.central.tw.entity.ErrorCodeEntity;
import tw.com.tbb.central.tw.repository.ErrorCodeRepository;

@Service
public class ErrorMessageService {

    @Autowired
    private ErrorCodeRepository errorCodeRepo;

    public String getMessage(String code) {
        return errorCodeRepo.findById(code)
                .map(ErrorCodeEntity::getMessage)
                .orElse("未知錯誤(" + code + ")");
    }
}
