package myDiplom.diplom.web;

import myDiplom.diplom.repository.MainRepository;
import myDiplom.diplom.web.pojos.Helper;
import myDiplom.diplom.web.pojos.RatesInfo;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.spring.JtwigViewResolver;
import org.jtwig.web.servlet.JtwigRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ViewResolver;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Controller
public class RatesController {

    @Autowired
    private MainRepository repository;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


    @GetMapping("/allRates")
    @ResponseBody
    public String[] rates() {
        return repository.getAllRates().stream().map(e->dateFormat.format(e.getDate())).toArray(String[]::new);
    }

    @GetMapping("/rates")
    @ResponseBody
    public RatesInfo rates(@RequestParam(value = "date")String date) throws ParseException {
        return Helper.convert(repository.getRate(dateFormat.parse(date)));
    }

    @GetMapping("/allCurrencies")
    @ResponseBody
    public Set<String> currencies() throws ParseException {
        return repository.getAllCurrency().stream().map(e->e.getName()).collect(Collectors.toSet());
    }

    @GetMapping("/Convert")
    @ResponseBody
    public double convert(@RequestParam(value = "date")String date,
                          @RequestParam(value = "from")String from,
                          @RequestParam(value = "to")String to,
                          @RequestParam(value = "count")String count) throws ParseException {
        return Helper.convertCurrency(Helper.convert(repository.getRate(dateFormat.parse(date))),from,to,count);
    }

    @GetMapping("/")
    public String weatherInfo(Model model) {
        model.addAttribute("data", repository.getAllRates());
        return "index";
    }

    @Bean // Подключаем Twig для java (Jtwig) для рендеринга страниц
    public ViewResolver viewResolver () {
        //Внимательно следим за конфигурацией
        EnvironmentConfiguration configuration = EnvironmentConfigurationBuilder
                .configuration()
                .parser()
                .syntax()
                .withStartCode("{%").withEndCode("%}")
                //Не надо портить AngularJS, он нам еще нужен
                .withStartOutput("{=").withEndOutput("=}")
                .withStartComment("{#").withEndComment("#}")
                .and()
                .and()
                .resources()
                // и кодировочку надо указать
                .withDefaultInputCharset(StandardCharsets.UTF_8)
                .and()
                .render()
                // и снова
                .withOutputCharset(StandardCharsets.UTF_8)
                .and()
                .build();

        JtwigRenderer jtwigRenderer = new JtwigRenderer(configuration);

        JtwigViewResolver viewResolver = new JtwigViewResolver();
        viewResolver.setRenderer(jtwigRenderer);
        //Красиво и по Java Web, но не работает при jar-упаковке
        //viewResolver.setPrefix("web:/WEB-INF/templates/");
        //у Spring Boot свой стандарт на этот случай
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".twig.html");
        viewResolver.setContentType("text/html; charset=UTF-8");
        return viewResolver;
    }
}
