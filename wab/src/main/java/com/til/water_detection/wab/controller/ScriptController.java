package com.til.water_detection.wab.controller;

import com.til.water_detection.data.Result;
import com.til.water_detection.data.state.ResultType;
import com.til.water_detection.wab.service.IScriptService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/script")
@Validated
@ResponseBody
public class ScriptController {

    @Resource
    private IScriptService scriptService;


    @PostMapping("/registerScript")
    public Result<Void> registerScript(@RequestBody String script) {
        int i = scriptService.registerScript(script);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @DeleteMapping("/removeScript")
    public Result<Void> removeScript( @RequestParam int id) {
        int i = scriptService.removeScript(id);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @PostMapping("/updateScriptById")
    public Result<Void> updateScriptById(@RequestParam int id, @RequestBody String script) {
        int i = scriptService.updateScriptById(id, script);
        return new Result<>(i == 0 ? ResultType.FAIL : ResultType.SUCCESSFUL, null, null);
    }

    @GetMapping("/getScriptById")
    public Result<String> getScriptById(@RequestParam int id) {
        String scriptById = scriptService.getScriptById(id);
        return new Result<>(scriptById == null ? ResultType.FAIL : ResultType.SUCCESSFUL, null, scriptById);
    }

}
