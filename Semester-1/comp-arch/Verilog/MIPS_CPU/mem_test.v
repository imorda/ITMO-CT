`include "memory.v"

module mem_tb;
  reg clk, we;
  reg[31:0] a;
  reg[31:0] wd;
  
  wire[31: 0] rd;
  
  data_memory mem(a, we, clk, wd, rd);
  
  always begin
    #5 clk = !clk;
    
    a = 0;
    a[5:0] = $random;
    wd = $random;
    we = $random;
  end
  
  initial begin
    clk = 0;
    $monitor("%b\t%b\t%32b\t%32b\t%32b", clk, we, a, wd, rd);
    #(5*20-1) $finish(0);
  end
endmodule