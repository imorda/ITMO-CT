module data_memory(a, we, clk, wd, rd);
  input we, clk;
  input [31:0] a;
  input [31:0] wd;
  output reg [31:0] rd;
  
  reg [31:0] ram[0:2047];
  
  integer i;
  initial begin
    for (i=0; i<2048; i = i+1) begin
      ram[i] = 0;
   	end
  end
  
  always @(a) begin
    rd = ram[a/4];
  end  
  
  always @ (posedge clk) begin
    if (we) ram[a/4] = wd;
    rd = ram[a/4];
  end
endmodule

module instruction_memory(a, rd);
  input [31:0] a;
  output reg [31:0] rd;

  // Note that at this point our programs cannot be larger then 64 subsequent commands.
  // Increase constant below if larger programs are going to be executed.
  reg [31:0] ram[0:63];

  initial
    begin
      $readmemb("instructions.dat", ram);
    end

  always @(a) begin
    rd = ram[a/4];
  end
endmodule

