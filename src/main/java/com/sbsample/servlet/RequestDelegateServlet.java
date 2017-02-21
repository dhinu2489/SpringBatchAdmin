package com.sbsample.servlet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by KumarDin on 2/15/2017.
 */
public class RequestDelegateServlet extends HttpServlet {


    public void init() throws ServletException
    {
        // Do required initialization

    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {

        String[] springConfig  =
                {	"spring/batch/config/database.xml",
                        "spring/batch/config/context.xml",
                        "spring/batch/jobs/job-report.xml"
                };
        PrintWriter out = response.getWriter();
        // Set response content type
        response.setContentType("text/html");

        //Ignoring Spring MVC auto-wiring as of now - to help Sid get the code done soon
        ApplicationContext context =
                new ClassPathXmlApplicationContext(springConfig);

        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("reportJob");

        try {

            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Exit Status : " + execution.getStatus());
            out.println("<h1>" + "Job executed Successfully"+ "</h1>");

        } catch (JobExecutionAlreadyRunningException| JobRestartException|
                JobInstanceAlreadyCompleteException|JobParametersInvalidException e) {
            e.printStackTrace();
            out.println("<h1>" + "Job ran into errors"+ "</h1>");
            out.println(e.getMessage());
        }


    }

    public void destroy()
    {
        // do nothing.
    }

}
