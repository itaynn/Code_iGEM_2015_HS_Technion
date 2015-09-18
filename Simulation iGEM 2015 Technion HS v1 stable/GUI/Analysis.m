function [rngs,peaks_idx,tms_idx] = Analysis( ms,ts ,threshold)
%ANALYSIS Summary of this function goes here
%   Detailed explanation goes here

A_rng=ms(1,1);
X_rng=ms(15,end);
N_rng=ms(14,end);
ccdb_rng=max(ms(13,:));
LA_rng=max(ms(6,:));
LA2_rng=max(ms(7,:));
TRLV_rng=max(ms(7,:));
try
    [pks,locs1] = findpeaks(ms(6,:)) ;
    LA_peak = locs1(1);
catch err
    warning(strcat('GUI:Analysis','Failed to compute LA_peak.',getReport(err)));
    LA_peak=-1;
end
try
    [pks,locs2] = findpeaks(ms(7,:)) ;
    LA2_peak = locs2(1);
catch err
    warning(strcat('GUI:Analysis','Failed to compute LA2_peak.',getReport(err)));
    LA2_peak = -1;
end
try
    [pks,locs3] = findpeaks(ms(10,:)) ;
    TRLV_peak = -1;
catch err
    warning(strcat('GUI:Analysis','Failed to compute TRLV_peak.',getReport(err)));
     TRLV_peak = locs3(1);
end
try
    [pks,locs4] = findpeaks(ms(13,:)) ;
    ccdb_peak = -1;
catch err
    warning(strcat('GUI:Analysis','Failed to compute ccdb_peak.',getReport(err)));
    ccdb_peak = -1;
end
try
    [pks,locs5] = find(ms(1,:)<LA2_peak*(1-threshold)) ;
    t1 = locs5(1);
catch err
    warning('GUI:Analysis','Failed to compute t1.');
end

rngs=[A_rng, X_rng,N_rng,ccdb_rng,LA_rng,LA2_rng,TRLV_rng];
peaks_idx=[LA_peak,LA2_peak,TRLV_peak,ccdb_peak];
tms_idx=[];
end

