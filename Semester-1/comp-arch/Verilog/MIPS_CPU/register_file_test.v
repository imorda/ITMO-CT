`include "register_file.v"

module register_tb;
  reg clk, we3;
  reg[4:0] a1, a2, a3;
  reg[31:0] wd3;
  
  wire[31: 0] rd1, rd2;
  
  register_file register(clk, we3, a1, a2, a3, wd3, rd1, rd2);
  
  always begin
    #5 clk = !clk;
    
    a1 = $random;
    a2 = a3;
    a3 = $random;
    wd3 = $random;
    we3 = $random;
  end
  
  initial begin
    clk = 0;
    $monitor("%b\t%b\t%4b\t%4b\t%4b\t%32b\t%32b\t%32b", clk, we3, a1, a2, a3, wd3, rd1, rd2);
    #(5*8-1) $finish(0);
  end
endmodule