package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// Removed "public" so Java compiler accepts it in this file
@RestController
class HelloController {

    @GetMapping("/")
    public String index() {
        return "<div style='font-family: Arial, sans-serif; text-align: center; padding: 60px 20px; background: #0f172a; color: #f8fafc; min-height: 100vh; display: flex; align-items: center; justify-content: center;'>"
             + "  <div style='background: #1e293b; max-width: 650px; padding: 40px; border-radius: 16px; border: 1px solid #334155; box-shadow: 0 10px 25px rgba(0,0,0,0.5);'>"
             + "    <h1 style='color: #38bdf8; font-size: 2rem; margin-bottom: 15px;'>🚀 Java Application Live on AWS EKS</h1>"
             + "    <p style='font-size: 1.1rem; line-height: 1.6; color: #cbd5e1;'>"
             + "      Hi! I am hosting a simple Java application built and deployed using a full <strong>Jenkins CI/CD pipeline</strong>. The container image is stored in <strong>AWS ECR</strong> and runs live on <strong>AWS EKS</strong>."
             + "    </p>"
             + "    <div style='margin-top: 25px; padding: 12px; background: #0f172a; border-radius: 8px; font-size: 0.9rem; color: #4ade80; font-family: monospace;'>"
             + "      Pipeline: GitHub &#10132; Jenkins &#10132; Amazon ECR &#10132; Amazon EKS"
             + "    </div>"
             + "  </div>"
             + "</div>";
    }
}
