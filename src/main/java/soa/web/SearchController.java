package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class SearchController {

	@Autowired
	  private ProducerTemplate producerTemplate;

	@RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping(value="/search")
    @ResponseBody
    public Object search(@RequestParam("q") String q) {
        String [] aux = q.split("max:"); //aux[0] = word, aux[1] = number
        boolean isNumber = true;
        try{
            Integer.parseInt(aux[1]);
        }catch (Exception e) {
            isNumber = false;
        }

        if (aux.length == 2 && isNumber){
            Map<String,Object> headers = new HashMap<String, Object>();
            headers.put("CamelTwitterKeywords", aux[0]);
            headers.put("CamelTwitterCount", aux[1]);
            return producerTemplate.requestBodyAndHeaders("direct:search", "", headers);
        }else{
            return producerTemplate.requestBodyAndHeader("direct:search", "", "CamelTwitterKeywords", q);
        }
    }
}