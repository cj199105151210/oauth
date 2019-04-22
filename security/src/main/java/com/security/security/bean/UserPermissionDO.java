package com.security.security.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Setter
@Getter
public class UserPermissionDO {

    private Integer id ;
    private Integer userId;
    private Integer roleId;
    private String roleName;
    public static void main(String[] args) {
        String greetingExp = "Hello, #{ #user }";
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", "Gangyou");
        Expression expression = parser.parseExpression(greetingExp,
                new TemplateParserContext());
        System.out.println(expression.getValue(context, String.class));
    }
}

