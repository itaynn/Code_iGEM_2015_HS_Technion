function [C,V,U,A,B,N_max] = setDefConst( )
%SETCONST Summary of this function goes here
%   Detailed explanation goes here
%  units!!!!!!!!

C=[10,  ...% c1,  aa_in,A_out to nothing rate
    0.01, ...% c2,  A_out to nothing rate
    1, ...% c3,  L_in,A_out to [L_in+A_out] rate
    0.1, ...% c4,  LA_out to L_in,A_out rate
    1, ...% c5,  LA_out,LA_out to LA2_out rate
    0.1, ...% c6,  LA2_out to LA_out,LA_out rate
    10, ...% c7,  a0,LA2_out to a1 rate
    0.1, ...% c8,  a1 to a0,LA2 rate
    10, ...% c9,  TRLV to nothing rate
    10, ...% c10, [b0+TRLV_out] to b1 rate
    0.1, ...% c11, b1 to b0,TRLV rate
    0.1, ...% c12, ccdb_out to nothing
    1, ...% c13, X produciong rate
    1, ...% c14, L_in producing rate
    0.1, ...% c15, L_in to nothing rate
    1, ...% c16, aa_in producing rate
    0.1  ...% c17, aa_in to nothing rate
    
    ];
V=[0.1, ...% v0, TRLV producing without LA2  
    1.5 ...% v1, TRLV producing with LA2  
    ];
U=[ 100, ...% u0, ccdb producing without TRLV
    0.01  ...% u1,  ccdb producing with TRLV
    ];
A=10; % A_RBS, of TRLV producing
B=1; % B_RBS, of ccdb producing
N_max=100;
end

%C=Const(1:17);V=Const(18:19);U=Const(20:21);A=Const(22);B=Const(23);
