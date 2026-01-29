package tw.com.tbb.central.tw.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.com.tbb.central.tw.api.n070.N070Rq;
import tw.com.tbb.central.tw.api.n070.N070Rs;
import tw.com.tbb.central.tw.api.n110.N110Rq;
import tw.com.tbb.central.tw.api.n110.N110Rs;
import tw.com.tbb.central.tw.api.n920.N920Rq;
import tw.com.tbb.central.tw.api.n920.N920Rs;
import tw.com.tbb.central.tw.api.n921.N921Rq;
import tw.com.tbb.central.tw.api.n921.N921Rs;
import tw.com.tbb.central.tw.service.N070Service;
import tw.com.tbb.central.tw.service.N110Service;
import tw.com.tbb.central.tw.service.N920Service;
import tw.com.tbb.central.tw.service.N921Service;

@Tag(name = "TW API", description = "台幣電文")
@RestController
@RequestMapping("/api/tw")
public class TwApi {

    @Autowired
    private N070Service n070Service;
    @Autowired
    private N110Service n110Service;
    @Autowired
    private N920Service n920Service;
    @Autowired
    private N921Service n921Service;

    // 自行台幣餘額查詢
    @Operation(summary = "N110 自行台幣餘額查詢")
    @PostMapping("/n110")
    public N110Rs n110(@Valid @RequestBody N110Rq rq) {
        return n110Service.queryBalance(rq);
    }

    // 自行台幣帳號查詢
    @Operation(summary = "N920 自行台幣帳號查詢")
    @PostMapping("/n920")
    public N920Rs n920(@Valid @RequestBody N920Rq rq) {
        return n920Service.queryAccounts(rq);
    }

    // 自行/他行 約定帳號查詢
    @Operation(summary = "N921 自行/他行 約定帳號查詢")
    @PostMapping("/n921")
    public N921Rs n921(@Valid @RequestBody N921Rq rq) {
        return n921Service.queryDesignatedAccounts(rq);
    }

    // 台幣轉帳
    @Operation(summary = "N070 台幣轉帳")
    @PostMapping("/n070")
    public N070Rs n070(@Valid @RequestBody N070Rq rq) {
        return n070Service.transfer(rq);
    }
}
