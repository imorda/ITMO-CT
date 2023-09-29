`include "memory.v"

module mem_tb;
  reg clk, we;
  reg[31:0] a;
  reg[31:0] wd;
  
  wire[31: 0] rd;
  
  instruction_memory mem(a, rd);
  
  integer i;
  initial begin
    $monitor("%32b", rd);
    for(i = 0; i < 87; i = i + 1)begin
      #1 a = i * 4;
    end
  end
endmodule