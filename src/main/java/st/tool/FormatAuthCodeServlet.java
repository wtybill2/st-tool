package st.tool;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormatAuthCodeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Random     r                = new Random();
    private static char[]     chs              = "1234567890".toCharArray();
    private static int        NUMBER_OF_CHS;
    private static int        IMG_WIDTH;
    private static int        IMG_HEIGHT;

    public void init(ServletConfig config) throws ServletException {
        String count = config.getInitParameter("count");
        NUMBER_OF_CHS = FormatEmpty.isEmpty(count) ? 4 : Integer.parseInt(count);
        String width = config.getInitParameter("width");
        IMG_WIDTH = FormatEmpty.isEmpty(width) ? 65 : Integer.parseInt(width);
        String height = config.getInitParameter("height");
        IMG_HEIGHT = FormatEmpty.isEmpty(height) ? 23 : Integer.parseInt(height);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB); // 实例化BufferedImage
        Graphics2D g = (Graphics2D) image.getGraphics();
        Color c = new Color(200, 200, 255); // 验证码图片的背景颜色
        g.setColor(c);
        g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT); // 图片的边框
        g.setFont(new Font("隶书", Font.BOLD, 15));// 设定字体

        StringBuffer sb = new StringBuffer(); // 用于保存验证码字符串
        int index; // 数组的下标
        for (int i = 0; i < NUMBER_OF_CHS; i++) {
            index = r.nextInt(chs.length); // 随机一个下标
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255))); // 随机一个颜色
            g.drawString(chs[index] + "", 15 * i + 3, 18); // 画出字符
            sb.append(chs[index]); // 验证码字符串
        }

        request.getSession().setAttribute("authCode", sb.toString()); // 将验证码字符串保存到session中
        ImageIO.write(image, "jpg", response.getOutputStream()); // 向页面输出图像
        // response.getWriter().write("aaaa");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
